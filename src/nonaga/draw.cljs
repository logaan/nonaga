(ns nonaga.draw
  (:use [nonaga.react :only [circle svg create-class render-component]])
  (:require [nonaga.core :as n]
            [nonaga.rules.ball :as b])
  (:use-macros [dommy.macros :only [sel1]]))

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
            "fill"    color
            "onClick" click
            "style"   {"cursor" (if click "pointer")}
            "key"     (str "marble" x "," y)})))

(defn draw [shape coords]
  (map (comp shape hex->svg) coords))

(defn draw-marbles [color coords click]
  (map #(marble color (click color %1) %2) coords (map hex->svg coords)))

(def light-colors
  {"red" "pink"
   "blue" "lightblue"})

(defn draw-valid-marble-moves [state]
  (let [[type & event-data] (:event state)]
    (when (= :marble-move type)
      (let [[color selected] event-data]
        (draw (partial marble (light-colors (name color)))
              (b/valid-destinations state selected))))))

(defn start-marble-move [component color coord]
  (fn []
    (let [old-state (.-wrapper (.-state component))
          new-state (assoc old-state :event [:marble-move color coord])]
      (.setState component #js {:wrapper new-state}))))

(def board
  (create-class
    "getInitialState"
    (fn [] #js {:wrapper n/initial-game})
    "render"
    (fn []
      (this-as this
         (let [state (.-wrapper (.-state this))]
           (svg {}
                (draw ring (:rings state))
                (draw-marbles "red"  (:whites state) (partial start-marble-move this))
                (draw-marbles "blue" (:blacks state) (partial start-marble-move this))
                (draw-valid-marble-moves state)))))))

(defn start []
  (render-component (board) (sel1 :#content)))

