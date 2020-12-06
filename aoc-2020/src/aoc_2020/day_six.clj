(ns aoc-2020.day-six
  (:require [clojure.string :as str])
  (:require [clojure.set]))

(def group-answers-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/group-answers.txt")

(defn aoc-day6-p1 []
  (let [file-contents    (slurp group-answers-file)
        group-answers (map #(str/replace % #"\r?\n" "") (str/split file-contents #"\r?\n\r?\n"))]
    (reduce + (map #(count (distinct %)) group-answers))))

(defn aoc-day6-p2 []
  (let [file-contents    (slurp group-answers-file)
        group-answers (map #(str/replace % #"\r?\n" " ") (str/split file-contents #"\r?\n\r?\n"))
        combined-groups (map #(str/split % #" ") group-answers)]
    (reduce + (map count (map #(reduce clojure.set/intersection (map set %)) combined-groups)))))
