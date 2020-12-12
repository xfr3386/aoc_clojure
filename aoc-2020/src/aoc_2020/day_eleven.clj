(ns aoc-2020.day-eleven
  (:require [clojure.string :as str]))
(def seating-values "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/seating_values.txt")
(def seating-values-sample "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/seating_values_sample.txt")

;; find the position in the array for a given row/column
(defn get-seat-position [row column num-cols]
  (+ (* row num-cols) column))

(defn build-adjacent-list [seats row column num-cols num-rows]
    (for [subrow (range (- row 1) (+ row 2))]
      (for [subcol (range (- column 1) (+ column 2))]
        (when (and (>= subrow 0) (>= subcol 0) (< subrow num-rows) (< subcol num-cols))
          (if (and (= row subrow) (= column subcol))
            nil
            (str (nth seats (get-seat-position subrow subcol num-cols)))))
        ))
  )

;; find the new state of the seat at (row,column) given the following rules:
;;    If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
;;    If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
;;    Otherwise, the seat's state does not change.
(defn find-new-state-of-seats [seats row column num-cols num-rows]
  (let [seat-position (get-seat-position row column num-cols)
        seat (str (nth seats seat-position))
        adjacent-seats (remove nil? (flatten (build-adjacent-list seats row column num-cols num-rows)))]
    (if (and (= seat "L") (not-any? #(= "#" %) adjacent-seats))
      "#"
      (if (and (= seat "#") (>= (count (filter #(= "#" %) adjacent-seats)) 4))
        "L"
        seat))
    )
  )

(defn find-new-state-of-seats-loop [seats num-cols num-rows]
  (for [row (range 0 num-rows)]
    (for [column (range 0 num-cols)]
      (find-new-state-of-seats seats row column num-cols num-rows))
    ))

(defn aoc-day11-p1 []
  (let [seating-file    (slurp seating-values)
        seats  (str/join (str/replace seating-file #"(\r\n)|\s" ""))
        seating-file-lines (str/split-lines seating-file)
        num-cols (count (first seating-file-lines))
        num-rows (count seating-file-lines)]

    (loop [previous-state ()
           current-state seats]

      (if (= current-state previous-state)
        (count (filter #(= "#" (str %)) current-state))
        (recur current-state (str/join (flatten (find-new-state-of-seats-loop current-state num-cols num-rows))))))))
