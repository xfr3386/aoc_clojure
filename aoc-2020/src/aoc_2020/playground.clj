(ns playground)

; print 0, -1, -2, -3, -4
(loop [i 0]
  (when (> i -5)
    (println i)
    (recur (dec i))
    ))

; sum of 10+9+8+7...
(loop [sum 0 cnt 10]
  (if (= cnt 0)
    sum
    (recur (+ cnt sum) (dec cnt))
    ))

; destructuring
(def names ["Bob" "Joe" "Sam" "Ned" "Homer" "Bart"])
(let [[item1 & remaining] names] ; item1 = Bob, remaining = the rest
      (println item1)
      (apply println remaining))