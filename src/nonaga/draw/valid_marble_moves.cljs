(ns nonaga.draw.valid-marble-moves
  (:require [nonaga.rules.ball :as b]
            [nonaga.core :as n])
  (:use [nonaga.draw.util :only [hex->svg marble update-state event-type]]))

(def light-colors
  {:red  :pink
   :blue :lightblue})

(defn move-marble [component color from to]
  (update-state component
    (fn [s] (let [s' (n/move-ball s color from to) 
                  winner (b/find-winner s')]
      (if (nil? winner)
        (assoc s' :event [:marble-moved color])
        (assoc s' :event [:game-won winner]))))))

(defmulti draw-valid-marble-moves #(event-type %2))

(defmethod draw-valid-marble-moves :default [_ _])

(defmethod draw-valid-marble-moves :marble-selected
  [component {[t color selected] :event :as state}]
  (map (fn [hex]
         (marble (light-colors color)
                 (move-marble component color selected hex)
                 (hex->svg hex)))
       (b/valid-destinations state selected)))

