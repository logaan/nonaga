(ns nonaga.core)

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

(defn nw [[x y]] (if (odd?  y) [x (+ y 1)] [(- x 1) (+ y 1)]))
(defn ne [[x y]] (if (even? y) [x (+ y 1)] [(+ x 1) (+ y 1)]))
(defn e  [[x y]]                           [(+ x 1) y      ])
(defn se [[x y]] (if (even? y) [x (- y 1)] [(+ x 1) (- y 1)]))
(defn sw [[x y]] (if (odd?  y) [x (- y 1)] [(- x 1) (- y 1)]))
(defn w  [[x y]]                           [(- x 1) y      ])

(defn invalid-space? [{:keys [rings whites blacks]} coord]
  (not (and (rings coord)
            (not (or (whites coord)
                     (blacks coord))))))

(defn move [board coord direction]
  (first
    (for [step (iterate direction coord)
          :when (invalid-space? board (direction step))]
      step)))

