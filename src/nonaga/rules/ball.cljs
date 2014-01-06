(ns nonaga.rules.ball
  (:use [nonaga.rules.coord :only [directions]]))

(defn invalid-space? [{:keys [rings red blue]} coord]
  (not (and (rings coord)
            (not (or (red coord)
                     (blue coord))))))

(defn move [board coord direction]
  (first
    (for [step (iterate direction coord)
          :when (invalid-space? board (direction step))]
      step)))

(defn valid-destinations [board coord]
  (disj (into #{} (map (partial move board coord) directions)) coord))

