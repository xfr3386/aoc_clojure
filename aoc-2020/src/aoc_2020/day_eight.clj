(ns aoc-2020.day-eight
  (:require [clojure.string :as str])
  (:require [clojure.edn :as edn]))

(def console-instructions-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/console-instructions.txt")
(def console-inst-sample-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/input-files/console-inst-sample.txt")

(defn perform-instructions
  ([cur-index all-instructions] (perform-instructions cur-index all-instructions () ()))
  ([cur-index all-instructions visited-indices acc-values]
   (let [full-instruction (nth all-instructions cur-index)
         instruction (re-find #"^\w*" full-instruction)
         instruction-value (Integer/parseInt (re-find #"[\+|-]\d+" full-instruction))]
     (if (some #(= cur-index %) visited-indices)
       acc-values
       (case instruction
         "nop" (perform-instructions (+ cur-index 1) all-instructions (conj visited-indices cur-index) acc-values)
         "jmp" (perform-instructions (+ cur-index instruction-value) all-instructions (conj visited-indices cur-index) acc-values)
         "acc" (perform-instructions (+ cur-index 1) all-instructions (conj visited-indices cur-index) (conj acc-values instruction-value)))))))

(defn aoc-day8-p1 []
  (let [instructions-file    (slurp console-instructions-file)
        main-file-contents  (str/split-lines instructions-file)]

    (println (reduce + (perform-instructions 0 main-file-contents)))))

(defn perform-instructions-terminate
  ([cur-index all-instructions inst-to-change] (perform-instructions-terminate cur-index all-instructions inst-to-change () ()))
  ([cur-index all-instructions inst-to-change visited-indices acc-values]
   (if (= cur-index (count all-instructions))
     acc-values
     (let [full-instruction (nth all-instructions cur-index)
           instruction (re-find #"^\w*" full-instruction)
           instruction-value (Integer/parseInt (re-find #"[\+|-]\d+" full-instruction))]
       (if (some #(= cur-index %) visited-indices)
         nil
         (case instruction
           "nop" (if (= full-instruction inst-to-change)
                   (perform-instructions-terminate (+ cur-index instruction-value) all-instructions inst-to-change (conj visited-indices cur-index) acc-values)
                   (perform-instructions-terminate (+ cur-index 1) all-instructions inst-to-change (conj visited-indices cur-index) acc-values))
           "jmp" (if (= full-instruction inst-to-change)
                   (perform-instructions-terminate (+ cur-index 1) all-instructions inst-to-change (conj visited-indices cur-index) acc-values)
                   (perform-instructions-terminate (+ cur-index instruction-value) all-instructions inst-to-change (conj visited-indices cur-index) acc-values))
           "acc" (perform-instructions-terminate (+ cur-index 1) all-instructions inst-to-change (conj visited-indices cur-index) (conj acc-values instruction-value))))))))

;; note this technically isn't valid because it replaces the first occurance of an instruction
;; not necessarily the one that it's looping on. however, it still finds the solution for the given sample data
(defn aoc-day8-p2 []
  (let [instructions-file    (slurp console-instructions-file)
        main-file-contents  (str/split-lines instructions-file)
        main-file-jmp (remove nil? (map #(re-find #"^jmp\s[\+|-]\d+.*$" %) main-file-contents))
        main-file-nop (remove nil? (map #(re-find #"^nop\s[\+|-]\d+.*$" %) main-file-contents))]
    
    ;; test replacing jmp instructions
    (loop [index 0]
      (if (= index (count main-file-jmp))
        nil
        (let [return-value (perform-instructions-terminate 0 main-file-contents (nth main-file-jmp index))]
          (if (some? return-value)
            (println (reduce + return-value))
            (recur (inc index))))))
             
    ;; test replacing nop instructions
    ;; (loop [index 0]
    ;;   (if (= index (count main-file-nop))
    ;;     nil
    ;;     (let [return-value (perform-instructions-terminate 0 main-file-contents (nth main-file-nop index))]
    ;;       (if (some? return-value)
    ;;         return-value
    ;;         (recur (inc index))))))
    )
  )
