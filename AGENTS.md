# AGENTS.md — AI agent instructions for Mediqueue

Purpose
- Provide concise, actionable guidance for AI coding agents working on this repository.

Quick facts
- Java: 21
- Spring Boot: 3+ (project uses Spring Boot 3.x)
- Build: use the wrapper `./mvnw` or `mvnw.cmd` on Windows
- Key files: [pom.xml](pom.xml), [HELP.md](HELP.md)
- Source: [src/main/java](src/main/java)

Agent-first rules (minimal, link-first)
- Read the project root files: [pom.xml](pom.xml) and [HELP.md](HELP.md) before coding.
- Link to existing docs; do not duplicate repository documentation.
- Keep changes small and focused; prefer edits scoped to a single feature or bug.

Coding conventions (enforced)
- Java 21, Spring Boot 3, Spring Data JPA with PostgreSQL.
- Use constructor injection only; do not use field injection.
- Controllers: only handle HTTP concerns.
- Services: contain business logic; use DTOs for input/output.
- Repositories: data access only.
- Never expose entities directly; use Mappers between DTOs and Entities.
- Use Bean Validation on DTOs and `@RestControllerAdvice` for exceptions.
- Use `ResponseEntity` for controllers and pagination for list endpoints.
- Security: JWT auth, BCrypt password encoder, role-based authorization.

Before modifying code
1. Review architecture: scan `src/main/java` for controllers, services, repositories.
2. Explain the problem and propose a focused solution in the PR description.
3. Wait for approval if the change is large or affects cross-cutting concerns (security, DB schema).

Files to check first
- [HELP.md](HELP.md)
- [pom.xml](pom.xml)
- [src/main/java](src/main/java)

Suggested next customizations
- Add `.github/copilot-instructions.md` if you want organization-level instructions.
- Add a small `AGENTS.md` section per major subsystem (api, service, persistence) if needed.

If you need more detail
- Ask for explicit approval to make larger architectural changes or upgrades.
