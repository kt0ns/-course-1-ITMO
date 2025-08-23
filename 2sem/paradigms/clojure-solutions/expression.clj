(defn constant [value] (fn [args] value))
(defn variable [vr] (fn [args] (double (get args (name vr)))))
      
(defn operation [op]
    (fn [& operands] 
        (fn [args] 
            (apply op (mapv (fn [operand] (operand args)) operands)))))      

(def negate (operation -))
(def subtract (operation -))
(def add (operation +))
(def multiply (operation *))
(def divide
    (operation (fn [arg1 arg2] 
        (/ (double arg1) (double arg2))
    )))

(def arcTan (operation Math/atan))
(def arcTan2 (operation Math/atan2))

(def list-of-operations {
   '+ add, 
   '- subtract, 
   '* multiply, 
   '/ divide, 
   'negate negate
   
   'atan arcTan
   'atan2 arcTan2
})

(defn parse-clojure-expr [token]
  (cond
    (symbol? token) (variable token)
    (number? token) (constant token)
    :else (apply (get list-of-operations (first token)) (mapv parse-clojure-expr (rest token)))))

(defn parseFunction [string] (parse-clojure-expr (read-string string)))



;===========================================================================================================================================================
;===============================================================================--- hw 11 object ---========================================================
;===========================================================================================================================================================

(definterface expr_token
  (evaluate [vars])
  (to_string []))

(deftype function [op symbol_of_op operands]
  expr_token
  (evaluate [this vars]
    (apply op (mapv (fn [operand] (.evaluate operand vars)) operands)))
  (to_string [this]
    (str "(" symbol_of_op " " (clojure.string/join " " (mapv (fn [operand] (.to_string operand)) operands)) ")"))
)

(defn create_function [op symbol_of_op]
  (fn [& operands] (function. op symbol_of_op operands))
)


(deftype constant_obj [value]
  expr_token
  (evaluate [this vars] value)
  (to_string [this] (str value))
)

(deftype variable_obj [variable]
  expr_token
  (evaluate [this vars] (vars variable))
  (to_string [this] (str variable))
)

(def Negate (create_function - "negate"))
(def Add (create_function + "+"))
(def Subtract (create_function - "-"))
(def Multiply (create_function * "*"))
(def Divide (create_function (fn [arg1 arg2] (/ (double arg1) (double arg2))) "/"))
(def Constant (fn [value] (constant_obj. value)))
(def Variable (fn [var] (variable_obj. var)))

(def ArcTan (create_function Math/atan "atan"))
(def ArcTan2 (create_function Math/atan2 "atan2"))

(def list_of_functions {
  'negate Negate
  '- Subtract
  '+ Add
  '* Multiply
  '/ Divide

  'atan ArcTan
  'atan2 ArcTan2
})

(def toString (fn [expr] (.to_string expr)))
(def evaluate (fn [expr vars] (.evaluate expr vars)))

(defn parse_clojure_expr_object [token]
  (cond
    (symbol? token) (Variable (str token))
    (number? token) (Constant token)
    :else (apply (list_of_functions (first token)) (mapv parse_clojure_expr_object (rest token)))))

(defn parseObject [string] (parse_clojure_expr_object (read-string string)))
