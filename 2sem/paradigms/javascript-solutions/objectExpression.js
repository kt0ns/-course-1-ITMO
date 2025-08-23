"use strict";

// :NOTE: нет позиций
const TYPE_OF_BRACKET = {
    OPEN: "OPEN",
    CLOSE: "CLOSE",
}
const VARIABLES = ['x', 'y', 'z'];

const defaultEvaluate = function (...values) {
    return this.typeOfOperation(...this.args.map(operand => operand.evaluate(...values)));
};

const defaultToString = function () {
    return this.args.map(operand => operand.toString()).join(" ") + " " + this.symbol;
};

const defaultPostfix = function () {
    return "(" + this.args.map(operand => operand.postfix()).join(" ") + " " + this.symbol + ")";
};

const defaultPrefix = function () {
    return "(" + this.symbol + " " + this.args.map(operand => operand.prefix()).join(" ") + ")";
};

function SetConstructorMethod(Constructor, setProperties, evaluate = defaultEvaluate, toString = defaultToString, prefix = defaultPrefix, postfix = defaultPostfix) {
    Constructor.prototype.evaluate = evaluate;
    Constructor.prototype.toString = toString;
    Constructor.prototype.prefix = prefix;
    Constructor.prototype.postfix = postfix;

    Constructor.SetProperties = setProperties;
    Constructor.SetProperties.prototype = Object.create(Constructor.prototype);
    Constructor.SetProperties.prototype.constructor = Constructor.SetProperties;

    return Constructor;
}

function Operation() {
}

SetConstructorMethod(
    Operation,
    function SetProperties(symbol, typeOfOperation, countOfArguments = typeOfOperation.length) {
        function CreateOperation(...args) {
            this.symbol = symbol;
            this.typeOfOperation = typeOfOperation;
            this.args = args;
        }

        CreateOperation.prototype = Object.create(SetProperties.prototype);
        CreateOperation.prototype.constructor = CreateOperation;

        CreateOperation.countOfArguments = countOfArguments;

        return CreateOperation;
    });

const Multiply = new Operation.SetProperties('*',
    (left, right) => left * right);

const Divide = new Operation.SetProperties('/',
    (left, right) => left / right);

const Add = new Operation.SetProperties('+',
    (left, right) => left + right);

const Subtract = new Operation.SetProperties('-',
    (left, right) => left - right);

const Negate = new Operation.SetProperties('negate',
    left => -left);

const Clamp = new Operation.SetProperties('clamp',
    (value, min, max) => {
        if (value < min) return min
        if (value > max) return max
        return value;
    });

const SoftClamp = new Operation.SetProperties('softClamp',
    (x, min, max, A) => min + (max - min) / (1 + Math.exp(A * ((max + min) / 2 - x))));


// :NOTE: сделать фабрику для const/ variable
function Variable(name) {
    this.evaluate = (x, y, z) => ({x, y, z}[name]);
    this.toString = () => name.toString();
    this.prefix = () => name.toString();
    this.postfix = () => name.toString();
}

function Const(value) {
    this.evaluate = () => Number(value);
    this.toString = () => value.toString();
    this.prefix = () => value.toString();
    this.postfix = () => value.toString();
}

const SumCb = new Operation.SetProperties('sumCb',
    (...args) => args.map(value => value * value * value).reduce((a, b) => a + b, 0));

const MeanCb = new Operation.SetProperties('meanCb',
    (...args) => args.map(value => value * value * value).reduce((a, b) => a + b, 0) / args.length);

const operations = new Map(
    [
        ['*', Multiply],
        ['/', Divide],
        ['+', Add],
        ['-', Subtract],
        ['negate', Negate],

        ['clamp', Clamp],
        ['softclamp', SoftClamp],

        ['sumcb', SumCb],
        ['meancb', MeanCb],
    ]
);

function parse(expression) {
    let stack = [];
    for (let token of expression.toString().trim().toLowerCase().split(/\s+/)) {
        let op;
        op = operations.has(token) ? new (operations.get(token))(...(stack.splice(-operations.get(token).countOfArguments))) :
            VARIABLES.includes(token) ? new (Variable)(token)
                : new (Const)(token);

        stack.push(op);
    }

    return stack.pop();
}


function AbstractError() {
    function Err(msg) {
        this.message = msg;
    }

    Err.prototype = Object.create(Error.prototype);
    Err.prototype.constructor = AbstractError;

    return Err;
}

const BracketError = AbstractError();
const ArgumentError = AbstractError();
const OperationError = AbstractError();


const reverseBool = (arr, bool) => bool ? arr.reverse() : arr;
const getBracket = function(typeOfBracket, isPrefix) {
    switch (typeOfBracket) {
        case TYPE_OF_BRACKET.OPEN:
            return isPrefix ? ')' : '('
        case TYPE_OF_BRACKET.CLOSE:
            return isPrefix ? '(' : ')'
        default:
            return ''
    }
}

function bracketParser(isPrefix = true) {
    const ArgError = function() {
        throw new ArgumentError('Invalid count of operands for operation in expression: "' + "(" + token + " " + localStack.map(arg => arg.toString()).join(" ") + ')". Ожидалось: ' + opInfo.countOfArguments);
    }
    return (expression) => {
        let tokens = reverseBool(
            expression.toString()
                .trim()
                .toLowerCase()
                .split(/([()\s])/g)
                .filter(el => !/^\s*$/.test(el)),
            isPrefix
        );
        let bracketBalance = 0;

        function parseExpression(tokens) {
            let localStack = [];
            let wasOp = false

            while (tokens.length > 0) {
                let token = tokens.shift();

                if (token === getBracket(TYPE_OF_BRACKET.OPEN, isPrefix)) {
                    bracketBalance++;
                    localStack.push(parseExpression(tokens));
                } else if (token === getBracket(TYPE_OF_BRACKET.CLOSE, isPrefix)) {
                    bracketBalance--;
                    if (bracketBalance < 0 ) {
                        throw new BracketError('Wrong bracket sequence balance');
                    }
                    if (localStack.length === 0) {
                        throw new BracketError('Empty op! ->()<-');
                    }
                    if (localStack.length === 1 && localStack[0] instanceof Operation) {
                        return localStack.pop();
                    }
                    throw new OperationError("No operation in expression: " + '(' + localStack.map(arg => arg.toString()).join(" ") + ')');
                } else if (operations.has(token)) {
                    if (wasOp) {
                        throw new OperationError('The operation has already been announced in expression: ' + "(WRONG->" + token + '<- ' + localStack.map(arg => arg.toString()).join(" ") + ')');
                    }

                    let opInfo = operations.get(token);

                    if (opInfo.countOfArguments > localStack.length) {
                        ArgError();
                    }
                    let op = new opInfo(...reverseBool(localStack.splice(-opInfo.countOfArguments), isPrefix));
                    if (localStack.length !== 0) {
                        ArgError();
                    }
                    localStack.push(op);
                    wasOp = true;
                } else if (VARIABLES.includes(token)) {
                    localStack.push(new Variable(token));
                } else if (!isNaN(Number(token))) {
                    localStack.push(new Const(token));
                } else {
                    throw new ArgumentError('Unexpected argument argument ->' + token + '<-');
                }
            }

            return localStack;
        }

        let result = parseExpression(tokens);
        if (bracketBalance !== 0) {
            throw new BracketError('Wrong bracket sequence balance');
        }
        if (result.length > 1) {
            throw new OperationError("not enough operations")
        }

        if (result.length === 0) {
            throw new ArgumentError("empty input");
        }
        return result.pop();
    }
}

const parsePrefix = bracketParser(true)
const parsePostfix = bracketParser(false)
