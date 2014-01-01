(ns nonaga.react
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [nonaga.core :as n])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(defn log [& values]
  (.log js/console (clj->js values)))

; --- react stuff ---
(defn svg [props child]
  (.svg (.-DOM js/React) (clj->js props) child))

(defn circle [props]
  (.circle (.-DOM js/React) (clj->js props)))

(defn create-class [& {:as config}]
  (.createClass js/React (clj->js config)))

(defn render-component [component node]
  (.renderComponent js/React component node))
; --- end react stuff ---

(defn hex-coord->svg-coord [[hex-x hex-y]]
  (let [x (+ 20 (* 40 hex-x) (if (odd? hex-y) 20 0))
        y (+ 20 (* 40 hex-y))]
    [x y]))

(defn ring [[x y :as coord]]
  (circle {"cx"           x
           "cy"           y
           "r"            14
           "fill"         "transparent"
           "stroke"       "grey"
           "strokeWidth" "7px"
           "key" (str "ring:" x "," y)}))

(defn draw-rings [coords]
  (->> coords
       (map hex-coord->svg-coord)
       (map ring)))

; This should be
; (defclass board
;   :render (fn [] ...))
(def board
  (create-class
    "render" (fn []
               (svg {}
                    (clj->js (draw-rings (:rings n/initial-game)))))))

(defn start []
  (render-component (board) (sel1 :#content)))

