(ns slipstream.ui.util.pattern-test
  (:require
    [clojure.test :refer :all]
    [slipstream.ui.util.localization :as localization]
    [slipstream.ui.util.pattern :as p]))

(def cip @#'slipstream.ui.util.pattern/case-insensitive-pattern)

(deftest test-case-insensitive-pattern
  (is (= "" (cip nil)))
  (is (= "" (cip "")))
  (is (= "[aA][bB]" (cip "ab")))
  (is (= "[aA][bB]" (cip "AB")))
  (is (= "[aA]|[bB]" (cip "a|b"))))

(def not-forbidden-user-roles-regex
  (localization/with-lang :en
    (->> :user-roles
         p/requirements
         (some #(when (-> % :pattern-name #{"not-forbidden-role"}) (:pattern %)))
         re-pattern)))

(defn- match-not-forbidden
  [matches? s]
  (let [expected  (when matches? s)
        actual    (re-matches not-forbidden-user-roles-regex s)]
    (is (= expected actual))))

(def check-invalid-roles  (partial match-not-forbidden false))
(def check-valid-roles    (partial match-not-forbidden true))

(deftest test-check-valid-roles
  (check-invalid-roles "ADMIN")
  (check-invalid-roles "admin")
  (check-invalid-roles "aDmIN")
  (check-invalid-roles "role")
  (check-invalid-roles "anon")
  (check-invalid-roles "user")
  (check-invalid-roles "toto, user")
  (check-invalid-roles "user, toto")

  (check-valid-roles "exoscale, tutu, tata")
  (check-valid-roles "adminXXX")
  (check-valid-roles "user123, tutu")
  (check-valid-roles "tutu, user123"))
