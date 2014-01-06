(ns nonaga.draw
  (:use [nonaga.react :only [circle svg create-class render-component]])
  (:require [nonaga.core :as n]
            [nonaga.rules.ball :as b])
  (:use-macros [dommy.macros :only [sel1]]))

; Events
;
; [:turn-began      :red]
; [:marble-selected :red coords]
; [:marble-moved    :red]
; [:ring-selected   :ring coords]
; [:turn-began      :blue]
; [:marble-selected :blue coords]
; [:marble-moved    :blue]
; [:ring-selected   :blue coords]

(defn hex->svg [[hex-x hex-y]]
  (let [width  40
        half (/ width 2)
        svg-x (+ half (* width hex-x) (if (odd? hex-y) half 0))
        svg-y (+ half (* width hex-y))]
    [svg-x svg-y]))

(defn ring
  ([coord] (ring nil coord))
  ([click [x y :as coord]]
   (circle {"cx"          x
            "cy"          y
            "r"           14
            "fill"        "transparent"
            "stroke"      "grey"
            "strokeWidth" "7px"
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

(defn draw [shape coords]
  (map (comp shape hex->svg) coords))

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

(defn draw-marbles [component state color]
  (let [current-player (get-in state [:event 1])
        coords         (color state)
        click          (partial start-marble-move component)]
    (map #(marble color (click color %1) %2) coords (map hex->svg coords))))

(def board
  (create-class
    "getInitialState"
    (fn []
      (let [initial-state (assoc n/initial-game :event [:turn-began :red])]
        #js {:wrapper initial-state}))
    "render"
    (fn []
      (this-as this
         (let [state (.-wrapper (.-state this)) ]
           (svg {}
                (draw ring (:rings state))
                (draw-marbles this state :red )
                (draw-marbles this state :blue)
                (draw-valid-marble-moves this state)))))))

(defn start []
  (render-component (board) (sel1 :#content)))

