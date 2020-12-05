(ns aoc-2020.day-five
  (:require [clojure.string :as str])
  (:require [clojure.set]))

(def seat-id-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/seat-ids.txt")

(def rows (range 128))
(def seats (range 8))

(defn find-position [search-term remaining-values comparator]
  (let [two-halves (partition (/ (count remaining-values) 2) remaining-values)]
    (if (= (count remaining-values) 1)
      (first remaining-values)
      (if (= (subs search-term 0 1) comparator)
          (find-position (subs search-term 1) (first two-halves) comparator)
          (find-position (subs search-term 1) (last two-halves) comparator)))))

(defn aoc-day5-p1 []
  (let [file-contents    (slurp seat-id-file)
        seat-ids (str/split-lines file-contents)]
    (println (apply max (map #(+ (* (find-position (subs % 0 7) rows "F") 8) (find-position (subs % 7 10) seats "L")) seat-ids)))))
  
(defn find-missing-number [list-of-numbers]
  (let [min-value (apply min list-of-numbers)
        max-value (apply max list-of-numbers)
        all-values (range min-value max-value)]
    (clojure.set/difference (set all-values) list-of-numbers)))

(defn aoc-day5-p2 []
  (let [file-contents    (slurp seat-id-file)
        seat-ids (str/split-lines file-contents)]
    (println (find-missing-number (map #(+ (* (find-position (subs % 0 7) rows "F") 8) (find-position (subs % 7 10) seats "L")) seat-ids)))))

