(ns nonaga.rules.rings
  (:require [clojure.set :refer [difference]])
  (:use [nonaga.rules.coord :only [neighbours neighbouring-directions tag-with-distance]]))

(defn valid-slide? [rings coord direction]
  (let [sliding-into-ring? (rings (direction coord))
        gap-too-small? (apply #(and %1 %2)
                              (map rings
                                   (map #(% coord)
                                        (neighbouring-directions direction))))]
    (not (or sliding-into-ring? gap-too-small?))))

(defn valid-slides [rings coord]
  (filter (partial valid-slide? rings coord)
          (keys neighbouring-directions)))

(defn neighbour-distances [rings source destination]
  (->> (valid-slides rings source)
       (map #(% source))
       (map (partial tag-with-distance destination))))

(defn can-move-to?
  ([rings source destination]
   (if (rings destination)
     false
     (can-move-to? rings
                   (sorted-set (tag-with-distance destination source))
                   (sorted-set)
                   destination
                   0)))
  ([rings unexploded exploded destination count]
   (let [next-choice    (first unexploded)
         [_ source]     next-choice
         explosion      (neighbour-distances rings source destination)
         new-exploded   (conj exploded next-choice) 
         new-unexploded (difference (into unexploded explosion) new-exploded)]
     (if (new-unexploded [0 destination]) true
       (if (empty? new-unexploded) false
         (if (> count 100) false
           (recur rings new-unexploded new-exploded destination (inc count))))))))

; Should remvoe the ring that is being moved
(defn valid-destinations [rings source]
  (let [other-rings (disj rings source)
        candidates (->> (frequencies (mapcat neighbours other-rings))
                        (filter (fn [[cell neighbours]] (< 1 neighbours 6)))
                        (map first)
                        (into #{}))
        availables (difference candidates rings)
        slidables  (filter (partial can-move-to? rings source) availables)]
    (set slidables)))

