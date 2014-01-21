(ns nonaga.draw.util)

(defn hex->svg [[hex-x hex-y]]
  (let [width  40
        half (/ width 2)
        svg-x (+ 160 (* width hex-x) (if (odd? hex-y) half 0))
        svg-y (+ 160 (* width hex-y))]
    [svg-x svg-y]))

