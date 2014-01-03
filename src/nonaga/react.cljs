(ns nonaga.react
  (:use [nonaga.wrapper :only [circle svg create-class render-component]])
  (:require [nonaga.core :as n])
  (:use-macros [dommy.macros :only [sel1]]))

(defn hex->svg [[hex-x hex-y]]
  (let [x (+ 20 (* 40 hex-x) (if (odd? hex-y) 20 0))
        y (+ 20 (* 40 hex-y))]
    [x y]))

(defn ring [[x y :as coord]]
  (circle {"cx"          x
           "cy"          y
           "r"           14
           "fill"        "transparent"
           "stroke"      "grey"
           "strokeWidth" "7px"
           "key"         (str "ring:" x "," y)}))

(defn marble [color [x y :as coord]]
  (circle {"cx"   x
           "cy"   y
           "r"    8
           "fill" color
           "key"  (str "marble" x "," y)}))

(defn draw [shape coords]
  (map (comp shape hex->svg) coords))

(def board
  (create-class
    "render" #(svg {}
                   (draw ring (:rings n/initial-game))
                   (draw (partial marble "red") (:whites n/initial-game))
                   (draw (partial marble "blue") (:blacks n/initial-game)))))

(defn start []
  (render-component (board) (sel1 :#content)))

