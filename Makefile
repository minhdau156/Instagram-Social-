dev:
	docker compose up -d
	cd backend && mvn spring-boot:run &
	cd frontend && npm run dev

test:
	cd backend && mvn test
	cd frontend && npm run test

migrate:
	cd backend && mvn flyway:migrate -Dspring.profiles.active=local

clean:
      docker compose down -v


.PHONY: dev test migrate clean
