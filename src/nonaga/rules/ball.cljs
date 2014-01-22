(ns nonaga.rules.ball
  (:use [nonaga.rules.coord :only [directions neighbours]]))

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

(defn find-winner [{:keys [rings red blue]}]
  (let [all-connected
	(fn [marble-color] 
	  (> 
	    (count 
		  (reduce clojure.set/union
		    (for [coord marble-color coord' marble-color] 
			  (clojure.set/intersection 
			    #{coord}
				(neighbours coord')))))
        2))]
      (if (all-connected red)
	    :red 
		  (if (all-connected blue)
		    :blue 
			nil))))