(ns nonaga.draw
  (:use [nonaga.react :only [circle svg create-class render-component]])
  (:require [nonaga.core :as n]
            [nonaga.rules.ball :as b]
            [nonaga.rules.rings :as r])
  (:use-macros [dommy.macros :only [sel1]]))

; Events
;
; [:turn-began :red]
; - Clickable red marbles
; [:marble-selected :red coords]
; - Clickable red marbles
; - Clickable pink marbles
; [:marble-moved :red]
; - Clickable moveable rings
; [:ring-selected :ring coords]
; - Clickable moveable rings
; - Clickable light grey rings
;
; [:turn-began :blue]
; - Clickable blue marbles
; [:marble-selected :blue coords]
; - Clickable blue marbles
; - Clickable pink marbles
; [:marble-moved :blue]
; - Clickable moveable rings
; [:ring-selected :ring coords]
; - Clickable moveable rings
; - Clickable light grey rings

(defn hex->svg [[hex-x hex-y]]
  (let [width  40
        half (/ width 2)
        svg-x (+ 100 (* width hex-x) (if (odd? hex-y) half 0))
        svg-y (+ 100 (* width hex-y))]
    [svg-x svg-y]))

(defn ring
  ([color coord] (ring color nil coord))
  ([color click [x y :as coord]]
   (circle {"cx"          x
            "cy"          y
            "r"           14
            "fill"        "transparent"
            "stroke"      color
            "strokeWidth" "7px"
            "style"       {"cursor" (if click "pointer")}
            "onClick"     click
            "key"         (str "ring:" x "," y)})))

(defn marble
  ([color coord] (marble color nil coord))
  ([color click [x y :as coord]]
   (circle {"cx"      x
            "cy"      y
            "r"       8
            "fill"    (name color)
            "onClick" click
            "style"   {"cursor" (if click "pointer")}
            "key"     (str "marble" x "," y)})))

(def opposite
  {:red :blue
   :blue :red})

(def light-colors
  {:red :pink
   :blue :lightblue})

(defn update-state [component update-fn]
  (fn []
    (let [old-state (.-wrapper (.-state component))]
      (.setState component #js {:wrapper (update-fn old-state)}))))

(defn move-marble [component color from to]
  (update-state component
                #(-> % (n/move-ball color from to)
                     (assoc :event [:marble-moved color]))))

(defn draw-valid-marble-moves [component state]
  (let [[type & event-data] (:event state)]
    (when (= :marble-selected type)
      (let [[color selected] event-data]
        (map (fn [hex]
               (marble (light-colors color)
                       (move-marble component color selected hex)
                       (hex->svg hex)))
              (b/valid-destinations state selected))))))

(defn start-marble-move [component color coord]
  (update-state component #(assoc % :event [:marble-selected color coord])))

; You shouldn't be able to move your marble once you've entered :marble-moved
(defn draw-marbles [component state color]
  (let [cp     (get-in state [:event 1])
        coords (color state)
        click  (partial start-marble-move component)]
    (map (fn [hex svg] (marble color (if (= cp color) (click color hex)) svg))
         coords (map hex->svg coords))))

(defn print-state []
  (js/console.log (str (.-wrapper (.-state js/board))))
  false)

; This is the same as start-marble-move
(defn ring-selected [component color coord]
  (update-state component #(assoc % :event [:ring-selected color coord])))

; To be able to be moved must have:
; - At least a gap of two
; - No ball on top
(defn draw-rings [component state]
  (let [[type & event-data] (:event state)
        coords (:rings state)]
    (map (fn [hex svg]
           (let [click (if (or (= :marble-moved type) (= :ring-selected type))
                         (let [[color] event-data]
                           (ring-selected component color hex)))]
             (ring "grey" click svg)))
         coords (map hex->svg coords))))

; These things should be a multi method that dispatches on state. Or I could
; have state records with a draw protocol.
(defn draw-potential-rings [component state]
  (let [[type & event-data] (:event state)
        coords (:rings state)]
    (if (= :ring-selected type)
      (let [[color coord] event-data]
        (map (comp (partial ring "lightgrey") hex->svg)
             (r/valid-destinations coords coord))))))

(def board
  (create-class
    "getInitialState"
    (fn []
      (this-as this
               (aset js/window "board" this)
               (let [initial-state (assoc n/initial-game :event [:turn-began :red])]
                 #js {:wrapper initial-state})))
    "render"
    (fn []
      (this-as this
         (let [state (.-wrapper (.-state this)) ]
           (svg {:width 400 :height 400}
                (draw-rings this state)
                (draw-potential-rings this state)
                (draw-marbles this state :red)
                (draw-marbles this state :blue)
                (draw-valid-marble-moves this state)))))))

(defn start []
  (render-component (board) (sel1 :#content)))

