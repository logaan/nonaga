(ns nonaga.draw.last-ring
  (:use [nonaga.draw.util :only [hex->svg ring]]))

(defn draw-last-ring [state]
  (if-let [last-ring (:last-ring state)]
    (ring "#000" (hex->svg last-ring))))      

