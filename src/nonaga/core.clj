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

(defn nw [[x y]] [(if (odd?  y) x (- x 1)) (+ y 1)])
(defn ne [[x y]] [(if (even? y) x (+ x 1)) (+ y 1)])
(defn w  [[x y]] [(- x 1) y])
(defn e  [[x y]] [(+ x 1) y])
(defn sw [[x y]] [(if (odd?  y) x (- x 1)) (- y 1)])
(defn se [[x y]] [(if (even? y) x (+ x 1)) (- y 1)])

(defn neighbours [cell]
  (into #{} ((juxt nw ne e se sw w) cell)))

; Ball
(defn invalid-space? [{:keys [rings whites blacks]} coord]
  (not (and (rings coord)
            (not (or (whites coord)
                     (blacks coord))))))

; Ball
(defn move [board coord direction]
  (first
    (for [step (iterate direction coord)
          :when (invalid-space? board (direction step))]
      step)))

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

(defn valid-slides [board coord]
  (filter (partial valid-slide? board coord)
          (keys neighbouring-directions)))

(defn distance [[x1 y1] [x2 y2]]
  (let [xdiff (Math/abs (- x1 x2))
        ydiff (Math/abs (- y1 y2))
        halfy (int (/ ydiff 2))]
    (if (> xdiff halfy)
      (+ xdiff halfy)
      ydiff)))

