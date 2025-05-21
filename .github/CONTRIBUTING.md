# Contribution Guide

Thank you for considering contributing to the Novalume project! This document provides guidelines and instructions for effectively contributing to the project.

## Git Workflow

We follow a feature branch-based workflow:

1. Create a branch from `develop`: `git checkout -b feature/feature-name` or `fix/bug-name`
2. Make your changes and commits using the commit conventions described below
3. Push your changes: `git push origin feature/feature-name`
4. Open a Pull Request following the template

## Commit Convention

We use [Conventional Commits](https://www.conventionalcommits.org/) for our commits:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Formatting, missing semicolons, etc; no code change
- `refactor`: Production code refactoring
- `test`: Adding tests, refactoring tests; no production code change
- `chore`: Updating build tasks, package manager configs, etc; no production code change

Example:

feat(catalog): add endpoint for category search

- Implements new GET /api/products/category/{id} endpoint
- Adds tests and documentation
- Updates OpenAPI documentation

Resolves #123

## Code Guidelines

### Java

- Follow [Google's Java code conventions](https://google.github.io/styleguide/javaguide.html)
- Maintain code coverage above 80%
- Document public classes and methods
- Write unit tests for all new features

## Pull Request Process

1. Ensure your branch is up to date with `develop` before submitting the PR
2. Fill out the PR template completely
3. Assign at least one reviewer
4. Respond to any review comments
5. After approval, a maintainer will merge your PR

## Reporting Bugs

- Use the bug report template
- Be specific about how to reproduce the issue
- Include screenshots, logs, or other relevant information

## Proposing Features

- Use the feature request template
- Clearly explain the problem your feature solves
- Consider how your proposal affects other parts of the system

## Questions?

If you have questions about the contribution process, open a discussion issue or contact the maintainer team.

Thank you for contributing!
