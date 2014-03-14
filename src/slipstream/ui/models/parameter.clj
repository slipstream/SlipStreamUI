(ns slipstream.ui.models.parameter)

(defn parse-parameter
  "Parses an absolute parameter name and returns a three-element
   vector containing the machine name, index, and full parameter
   name; if the parameter is not absolute then [nil nil pname] is
   returned.  This function is tolerant of leading or trailing
   whitespace, which will be stripped from the result."
  [pname]
  (if-let [v (re-matches #"\s*([\w\.-]+?)(?:\.(\d+))?:(\S*)\s*" pname)]
    (rest v)
    [nil nil (.trim pname)]))

(defn param-name
  [qualified-param]
  (-> qualified-param
      parse-parameter
      last))

(defn url-param?
  [p]
  (-> p
      param-name
      (or "")
      (.startsWith "url.")
      boolean))
