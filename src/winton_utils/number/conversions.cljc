(ns winton-utils.number.conversions)

(defn parse-integer
  "Parse a string int into an int.
  The regexp check is for security since read-string can evaluate arbitrary code."
  [s]
  (when-let [si (re-find #"^\s*-?\d+" s)]
    (read-string si)))


#_(comment                                                    ; parse-integer tests

  ;; a cljc way to read-integers
  (parse-integer "12345")
  ;=> 12345

  ; handles bigints
  (parse-integer "12345123451234512345")
  ;=> 12345123451234512345N

  (parse-integer "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345")
  => 1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345N

  ; and negatives
  (parse-integer "-1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345")
  => -1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345N

  (parse-integer "watcha")
  => nil

  )

(defn parse-number
  "Parse a string into a number"
  [s]
  (when-let [[sd _] (re-find #"^\s*-?\d+(\.\d+)?([eE](-|\+)?\d{1,3})?" s)]
    (read-string sd)))


#_(comment                                                   ; parse-number tests

  (parse-number "")
  => nil

  (parse-number "watcha")
  => nil

  (parse-number "1watcha")
  => 1

  (parse-number "watcha1")
  => nil

  (parse-number "1.")
  => 1

  (parse-number "1.0")
  => 1.0

  (parse-number "000")
  0

  (parse-number "0.25")
  0.25

  (parse-number "0.12345678912345678")
  => 0.12345678912345678

  (parse-number "0.12345678912345678e-13")
  => 1.2345678912345679E-14

  (parse-number "1.2345678912345679E-14")
  => 1.2345678912345679E-14

  (parse-number "1e13")
  => 1.0E13

  (parse-number "1e+13")
  => 1.0E13

  (parse-number "1e+999")
  => Infinity

  (parse-number "1e-999")
  => 0.0

  (parse-number "-0")
  0

  (parse-number "--6")
  nil

  )