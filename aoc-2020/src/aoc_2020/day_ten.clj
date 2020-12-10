(ns aoc-2020.day-ten
  (:require [clojure.string :as str]))

(def joltage-values "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/joltage_values.txt")
(def joltage-values-sample "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/joltage_values_sample.txt")

(defn aoc-day10-p1 []
  (let [joltage-file    (slurp joltage-values)
        joltage-file-contents  (sort < (map read-string (str/split-lines joltage-file)))
        total-adapters (count joltage-file-contents)
        device-joltage (+ (last joltage-file-contents) 3)]
    (println joltage-file-contents)
    (println device-joltage)

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
          (do
            (println one-jolt-diffs)
            (println two-jolt-diffs)
            (println (conj three-jolt-diffs 3))
            (* (count one-jolt-diffs) (+ (count three-jolt-diffs) 1)))
          (let [adapter-difference (- (nth joltage-file-contents (+ index 1)) (nth joltage-file-contents index))]
            (println "adapter-diff: " adapter-difference)
            (if (= adapter-difference 1)
              (recur (inc index) (conj one-jolt-diffs 1) two-jolt-diffs three-jolt-diffs)
              (if (= adapter-difference 2)
                (recur (inc index) one-jolt-diffs (conj two-jolt-diffs 2) three-jolt-diffs)
                (recur (inc index) one-jolt-diffs two-jolt-diffs (conj three-jolt-diffs 3))))))))

        ;; wall outlet is 0
    ;; device is highest +3
    ;; each one must be 1-3 lower than the next
    ;; find all 1, 2 and 3 jolt differences between the adapters in the file
    ;; multiple the number of 1-jolt with the number of 3-jolt
    )
  )
