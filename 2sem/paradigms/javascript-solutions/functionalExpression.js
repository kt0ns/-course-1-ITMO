"use strict";

// :NOTE:  const
let operation = (operation) => (...args) => (...values) => operation(...(args.map(operand => operand(...values))));
let variable = variable => (...values) => values[typesOfVariables.indexOf(variable)];
let cnst = num => (...values) => num
let add = operation((a, b) => a + b)
let subtract = operation((a, b) => a - b)
let multiply = operation((a, b) => a * b)
let divide = operation((a, b) => a / b)
let negate = operation(a => -a)

let cosh = operation(Math.cosh)
let sinh = operation(Math.sinh)
let power = operation(Math.pow)
let log = operation((a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)))

let less3 = operation((a, b, c) => Number(a < b && b < c))
let greater4 = operation((a, b, c, d) => Number(a > b && b > c && c > d))
let lessEq2 = operation((a, b) => Number(a <= b));
let greaterEq5 = operation((a, b, c, d, e) => Number(a >= b && b >= c && c >= d && d >= e))

let tau = cnst(2 * Math.PI)
let phi = cnst((1 + Math.sqrt(5)) / 2)

let operations = new Map(
    [
        ['*', multiply],
        ['/', divide],
        ['+', add],
        ['-', subtract],
        ['negate', negate],
        ['cosh', cosh],
        ['sinh', sinh],
        ['pow', power],
        ['log', log],
        ['less3', less3],
        ['greater4', greater4],
        ['lesseq2', lessEq2],
        ['greatereq5', greaterEq5]
    ]
);

let countOfArgs = new Map(
    [
        ['*', 2],
        ['/', 2],
        ['+', 2],
        ['-', 2],
        ['negate', 1],
        ['cosh', 1],
        ['sinh', 1],
        ['pow', 2],
        ['log', 2],
        ['less3', 3],
        ['greater4', 4],
        ['lesseq2', 2],
        ['greatereq5', 5]
    ]
);

let typesOfConstants = new Map(
    [
        ['tau', cnst(2 * Math.PI)],
        ['phi', cnst((1 + Math.sqrt(5)) / 2)],
    ]
);

const typesOfVariables = ['x', 'y', 'z', 't'];

function parse(expression) {
    let stack = [];
    for (let token of expression.toString().trim().toLowerCase().split(/\s+/)) {

        let op
        if (operations.has(token)) {
            op = operations.get(token)(...(stack.splice(-countOfArgs.get(token))))
        } else if (typesOfVariables.includes(token)) {
            op = variable(token);
        } else if (typesOfConstants.has(token)) {
            op = typesOfConstants.get(token)
        } else {
            op = cnst(Number(token));
        }

        stack.push(op);

    }
    return stack.pop();
}
