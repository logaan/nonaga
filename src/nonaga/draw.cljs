(ns nonaga.draw
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(extend-type object
   dommy.template/PElement
   (-elem [this] this))

(defn log [stuff]
  (.log js/console (clj->js stuff)))

(defn make-svg [tag & attrs]
  (let [element (.createElementNS js/document "http://www.w3.org/2000/svg" tag)
        args (conj attrs element)]
    (apply dommy/set-attr! args)))

(defn make-text [x y text]
  (let [elem (.createElementNS js/document "http://www.w3.org/2000/svg" "text")]
    (-> elem
        (dommy/set-text! text)
        (dommy/set-attr! :x x :y y))))

(def svg
  (make-svg "svg"
            :xmlns "http://www.w3.org/2000/svg"
            :viewbox "0 0 1200 800"
            :width  "200px"
            :height "200px"))

(defn ring [x y]
  (make-svg "circle"
            :cx x
            :cy y
            :r 14
            :fill "transparent"
            :stroke "grey"
            :stroke-width "7px"))

(defn marble [x y color]
  (make-svg "circle"
            :cx x
            :cy y
            :r 8
            :fill color))

(dommy/append! (sel1 :body) svg)

(def rings
  (list
       [1 4] [2 4] [3 4]
     [0 3] [1 3] [2 3] [3 3]
   [0 2] [1 2] [2 2] [3 2] [4 2]
     [0 1] [1 1] [2 1] [3 1]
       [1 0] [2 0] [3 0]))


(def reds
  (list
       [1 4]

                           [4 2]

       [1 0]))

(def blues
  (list
                   [3 4]

   [0 2]

                   [3 0]))


(defn hex-coord->svg-coord [[hex-x hex-y]]
  (let [x (+ 20 (* 40 hex-x) (if (odd? hex-y) 20 0))
        y (+ 20 (* 40 hex-y))]
    [x y]))

(defn coord->str [[x y]]
  (str x "," y))

; Could make these functional by removing svg-node and append!
(defn draw-rings [svg-node coords]
  (->> coords
       (map hex-coord->svg-coord)
       (mapv (fn [[x y]] (dommy/append! svg-node (ring x y))))))

(defn draw-marbles [svg-node coords color]
  (->> coords
       (map hex-coord->svg-coord)
       (mapv (fn [[x y]] (dommy/append! svg-node (marble x y color))))))

(defn draw-coords [svg-node coords]
  (letfn [(draw [coord [x y]]
            (let [text (make-text (- x 10) (+ y 5) (coord->str coord))]
              (dommy/append! svg-node text)))]
    (->> coords
         (map hex-coord->svg-coord)
         (mapv draw coords))))

(draw-marbles svg reds "red")
(draw-marbles svg blues "blue")
(draw-rings svg rings)

; (draw-coords svg rings)

