(ns aoc-2020.day-four
  (:require [clojure.string :as str])
  (:require [clojure.edn :as edn]))

(def passport-file "/Users/amund/Projects/aoc_clojure/aoc-2020/src/aoc_2020/passports.txt")

;cid (Country ID) - optional value
(defn check-cid [passport-to-check]
  (if (nil? (get passport-to-check :cid))
    (if (= (count passport-to-check) 7)
      passport-to-check
      nil)
    (if (= (count passport-to-check) 8)
      passport-to-check
      nil)
    ))

(defn aoc-day4-p1 []
  (let [file-contents    (slurp passport-file)
        passports (map #(str/replace % #"\r?\n" " ") (str/split file-contents #"\r?\n\r?\n"))
        mapped-passport-strings (map #(str/replace % #"(\w{3}+)(:)([\w\#]+)" "$2$1 \"$3\"") passports)
        mapped-passports (map #(edn/read-string (str "{" % "}")) mapped-passport-strings)]    
    (time (count (remove nil? (reduce (fn [valid-passports valid-passport]
                                        (into valid-passports (set [(check-cid valid-passport)])))
                                      []
                                      mapped-passports))))
    ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;byr (Birth Year) - four digits; at least 1920 and at most 2002.
(defn check-byr [passport-to-check]
  (let [birth-year (get passport-to-check :byr)]
    (if (and (some? birth-year) (<= 1920 (Integer/parseInt birth-year) 2002))
      passport-to-check
      nil
      )))

;iyr (Issue Year) - four digits; at least 2010 and at most 2020.
(defn check-iyr [passport-to-check]
  (let [issue-year (get passport-to-check :iyr)]
    (if (and (some? issue-year) (<= 2010 (Integer/parseInt issue-year) 2020))
      passport-to-check
      nil)))

;eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
(defn check-eyr [passport-to-check]
  (let [expiration-year (get passport-to-check :eyr)]
    (if (and (some? expiration-year) (<= 2020 (Integer/parseInt expiration-year) 2030))
      passport-to-check
      nil)))

;hgt (Height) - a number followed by either cm or in:
;   If cm, the number must be at least 150 and at most 193.
;   If in, the number must be at least 59 and at most 76.
(defn check-hgt [passport-to-check]
  (let [height-unit (get passport-to-check :hgt)]
    (if (nil? height-unit)
      nil
      (let [height (Integer/parseInt (re-find #"\d+" height-unit))
            unit (re-find #"[a-zA-Z]+" height-unit)]
      (if (or (and (= unit "cm") (<= 150 height 193))
              (and (= unit "in") (<= 59 height 76)))
          passport-to-check
          nil))
)))

;hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
(defn check-hcl [passport-to-check]
  (let [hair-color (get passport-to-check :hcl)]
    (if (and (some? hair-color) 
             (= (count hair-color) 7)
             (re-find #"\#[0-9a-f]{6}" hair-color))
      passport-to-check
      nil)))

;ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
(defn check-ecl [passport-to-check]
  (let [eye-color (get passport-to-check :ecl)]
   (if (and (some? eye-color)
            (re-find #"(amb|blu|brn|gry|grn|hzl|oth)" eye-color))
     passport-to-check
     nil)))

;pid (Passport ID) - a nine-digit number, including leading zeroes.
(defn check-pid [passport-to-check]
  (let [passport-id (get passport-to-check :pid)]
   (if (and (some? passport-id)
            (re-find #"^\d{9}$" passport-id))
     passport-to-check
     nil)))

(defn get-valid-passport-p2 [passport-to-check]
  (if (and (check-cid passport-to-check) (check-byr passport-to-check)
           (check-iyr passport-to-check) (check-eyr passport-to-check)
           (check-hgt passport-to-check) (check-hcl passport-to-check)
           (check-ecl passport-to-check) (check-pid passport-to-check))
    passport-to-check
    nil)
  )

(defn aoc-day4-p2 []
  (let [file-contents    (slurp passport-file)
        passports (map #(str/replace % #"\r?\n" " ") (str/split file-contents #"\r?\n\r?\n"))
        mapped-passport-strings (map #(str/replace % #"(\w{3}+)(:)([\w\#]+)" "$2$1 \"$3\"") passports)
        mapped-passports (map #(edn/read-string (str "{" % "}")) mapped-passport-strings)]    
    (time (count (remove nil? (reduce (fn [valid-passports valid-passport]
                                        (into valid-passports (set [(get-valid-passport-p2 valid-passport)])))
                                      []
                                      mapped-passports))))
    )
  )