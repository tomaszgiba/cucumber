import LintStream from '../src/lint_stream'

describe(LintStream.name, () => {
  it("writes an error when a rule fails", cb => {
    const failingRules = [(uri, gherkinDocument) => Promise.reject(new RuleViolation())]
    const lintStream = new LintStream(failingRules)
  })
})