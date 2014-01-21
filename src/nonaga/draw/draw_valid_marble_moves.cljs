(ns nonaga.draw.draw-valid-marble-moves
  (:require [nonaga.rules.ball :as b]
            [nonaga.core :as n])
  (:use [nonaga.draw.util :only [hex->svg marble update-state]]))

(def light-colors
  {:red :pink
   :blue :lightblue})

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
