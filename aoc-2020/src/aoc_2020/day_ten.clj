(ns aoc-2020.day-ten
  (:require [clojure.string :as str])
  (:require [clojure.math.numeric-tower :as math]))

(def joltage-values "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/joltage_values.txt")
(def joltage-values-sample "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/joltage_values_sample.txt")

(defn aoc-day10-p1 []
  (let [joltage-file    (slurp joltage-values)
        joltage-file-contents  (sort < (map read-string (str/split-lines joltage-file)))
        total-adapters (count joltage-file-contents)]
    (loop [index -1
           one-jolt-diffs ()
           two-jolt-diffs ()
           three-jolt-diffs ()]

    ;; add the first adapter's difference betwee it and the wall
      (if (= index -1)
        (if (= (first joltage-file-contents) 1)
          (recur (inc index) (conj one-jolt-diffs 1) two-jolt-diffs three-jolt-diffs)
          (if (= (first joltage-file-contents) 2)
            (recur (inc index) one-jolt-diffs (conj two-jolt-diffs 2) three-jolt-diffs)
            (recur (inc index) one-jolt-diffs two-jolt-diffs (conj three-jolt-diffs 3))))

        (if (= index (- total-adapters 1))
          (println (* (count one-jolt-diffs) (+ (count three-jolt-diffs) 1)))
          (let [adapter-difference (- (nth joltage-file-contents (+ index 1)) (nth joltage-file-contents index))]
            (if (= adapter-difference 1)
              (recur (inc index) (conj one-jolt-diffs 1) two-jolt-diffs three-jolt-diffs)
              (if (= adapter-difference 2)
                (recur (inc index) one-jolt-diffs (conj two-jolt-diffs 2) three-jolt-diffs)
                (recur (inc index) one-jolt-diffs two-jolt-diffs (conj three-jolt-diffs 3))))))))))

(defn compute-combinations [separated-diffs]
  (loop [index 0
         two-groups 0
         three-groups 0
         four-groups 0]
    (if (= index (count separated-diffs))
      (* (math/expt 2 two-groups) (math/expt 4 three-groups) (math/expt 7 four-groups))
      (let [current-group (nth separated-diffs index)]
        (if (and (some #(= 1 %) current-group) (> (count current-group) 1))
          (if (= (count current-group) 2)
            (recur (inc index) (inc two-groups) three-groups four-groups)
            (if (= (count current-group) 3)
              (recur (inc index) two-groups (inc three-groups) four-groups)
              (recur (inc index) two-groups three-groups (inc four-groups))))
          (recur (inc index) two-groups three-groups four-groups))))))

(defn aoc-day10-p2 []
  (let [joltage-file    (slurp joltage-values)
        joltage-file-contents  (sort < (into [] (map read-string (str/split-lines joltage-file))))
        total-adapters (count joltage-file-contents)]
    (loop [index -1
           all-jolt-diffs ()]

    ;; add the first adapter's difference between it and the wall
      (if (= index -1)
        (if (= (first joltage-file-contents) 1)
          (recur (inc index) (conj all-jolt-diffs 1))
          (recur (inc index) (conj all-jolt-diffs 3)))

        (if (= index (- total-adapters 1))
          (println (compute-combinations (partition-by #(= 1 %) all-jolt-diffs)))
          (let [adapter-difference (- (nth joltage-file-contents (+ index 1)) (nth joltage-file-contents index))]
            (if (= adapter-difference 1)
              (recur (inc index) (conj all-jolt-diffs 1))
              (recur (inc index) (conj all-jolt-diffs 3)))))))))

