(ns nonaga.core
  (:require [clojure.set :refer [difference]]))

(def initial-game
  {:rings
    #{   [1 4] [2 4] [3 4]  
       [0 3] [1 3] [2 3] [3 3]
     [0 2] [1 2] [2 2] [3 2] [4 2] 
       [0 1] [1 1] [2 1] [3 1] 
         [1 0] [2 0] [3 0]}
  
   :whites
    #{   [1 4]
    
                             [4 2] 
     
         [1 0]}
  
   :blacks
    #{               [3 4]  
       
     [0 2]
       
                     [3 0]}})

(defn nw [[x y]] [(if (odd?  y) x (- x 1)) (+ y 1)])
(defn ne [[x y]] [(if (even? y) x (+ x 1)) (+ y 1)])
(defn w  [[x y]] [(- x 1) y])
(defn e  [[x y]] [(+ x 1) y])
(defn sw [[x y]] [(if (odd?  y) x (- x 1)) (- y 1)])
(defn se [[x y]] [(if (even? y) x (+ x 1)) (- y 1)])

; Coord
(def directions
  [nw ne e se sw w])

(defn neighbours [cell]
  (into #{} ((apply juxt directions) cell)))

(defn invalid-space? [{:keys [rings whites blacks]} coord]
  (not (and (rings coord)
            (not (or (whites coord)
                     (blacks coord))))))

(defn move [board coord direction]
  (first
    (for [step (iterate direction coord)
          :when (invalid-space? board (direction step))]
      step)))

(defn valid-destinations [board coord]
  (disj (into #{} (map (partial move board coord) directions)) coord))

(def neighbouring-directions
  {nw [ne w]  ne [nw e]
   w  [nw sw] e  [ne se]
   sw [se w]  se [sw e]})

; Ring
; Should not be sliding into another ring.
; Should not be sliding between two rings.
(defn valid-slide? [{:keys [rings]} coord direction]
  (let [sliding-into-ring? (rings (direction coord))
        gap-too-small? (apply #(and %1 %2)
                              (map rings
                                   (map #(% coord)
                                        (neighbouring-directions direction))))]
    (not (or sliding-into-ring? gap-too-small?))))

; Ring
(defn valid-slides [board coord]
  (filter (partial valid-slide? board coord)
          (keys neighbouring-directions)))

; Coord
(defn distance [[x1 y1] [x2 y2]]
  (let [xdiff (Math/abs (- x1 x2))
        ydiff (Math/abs (- y1 y2))
        halfy (int (/ ydiff 2))]
    (if (> xdiff halfy)
      (+ xdiff halfy)
      ydiff)))

(defn tag-with-distance [destination source]
  [(distance source destination) source])

(defn neighbour-distances [grid source destination]
  (->> (valid-slides grid source)
       (map #(% source))
       (map (partial tag-with-distance destination))))

(defn move-towards
  ([board source destination]
   (if ((:rings board) destination)
     false
     (move-towards board
                   (sorted-set (tag-with-distance destination source))
                   (sorted-set)
                   destination
                   0)))
  ([board unexploded exploded destination count]
   (let [next-choice    (first unexploded)
         [_ source]     next-choice
         explosion      (neighbour-distances board source destination)
         new-exploded   (conj exploded next-choice) 
         new-unexploded (difference (into unexploded explosion) new-exploded)]
     (if (new-unexploded [0 destination]) true
       (if (empty? new-unexploded) false
         (if (> count 500) false
           (recur board new-unexploded new-exploded destination (inc count))))))))

