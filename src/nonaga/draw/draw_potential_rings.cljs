(ns nonaga.draw.draw-potential-rings
  (:require [nonaga.core :as n]
            [nonaga.rules.rings :as r])
  (:use [nonaga.draw.util :only [hex->svg ring update-state event-type]]))

(def opposite
  {:red :blue
   :blue :red})

(defn move-ring [component color from to]
  (update-state component
                #(-> % (n/move-ring from to)
                     (assoc :last-ring to)
                     (assoc :event [:turn-began (opposite color)]))))

; Could probably use an event type method
(defmulti draw-potential-rings #(event-type %2))

(defmethod draw-potential-rings :default [_ _])

(defmethod draw-potential-rings :ring-selected
  [component {[t color source] :event coords :rings}]
  (let [destinations (r/valid-destinations coords source)]
    (map (fn [hex svg]
           (ring "#DDD" (move-ring component color source hex) svg))
         destinations (map hex->svg destinations))))

