(ns nonaga.draw.draw-potential-rings
  (:require [nonaga.core :as n]
            [nonaga.rules.rings :as r])
  (:use [nonaga.draw.util :only [hex->svg ring update-state]]))

; move-ring
(def opposite
  {:red :blue
   :blue :red})

; Same as move-marble
; draw-potential-rings
(defn move-ring [component color from to]
  (update-state component
                #(-> % (n/move-ring from to)
                     (assoc :last-ring to)
                     (assoc :event [:turn-began (opposite color)]))))

; These things should be a multi method that dispatches on state. Or I could
; have state records with a draw protocol.
; render. own ns.
(defn draw-potential-rings [component state]
  (let [[type & event-data] (:event state)
        coords (:rings state)]
    (if (= :ring-selected type)
      (let [[color source] event-data
            destinations (r/valid-destinations coords source)]
        (map (fn [hex svg]
               (ring "#DDD" (move-ring component color source hex) svg))
             destinations (map hex->svg destinations))))))
