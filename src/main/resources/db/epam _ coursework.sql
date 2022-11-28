CREATE SCHEMA "project";

CREATE TYPE "project"."user_type" AS ENUM (
  'student',
  'teacher',
  'administrator'
);

CREATE TYPE "project"."status" AS ENUM (
  'not_started',
  'in_progress',
  'ended'
);

CREATE TABLE "project"."users" (
  "id" SERIAL PRIMARY KEY,
  "login" varchar(100) UNIQUE NOT NULL,
  "email" varchar(100) UNIQUE NOT NULL,
  "password" varchar(64) UNIQUE NOT NULL,
  "first_name" varchar(100) NOT NULL,
  "last_name" varchar(100) NOT NULL,
  "phone_number" varchar(13) UNIQUE NOT NULL,
  "user_type" "project"."user_type" NOT NULL,
  "is_blocked" boolean NOT NULL,
  "send_notification" boolean NOT NULL
);

CREATE TABLE "project"."courses" (
  "id" SERIAL PRIMARY KEY,
  "topic_id" int,
  "name" varchar(100) NOT NULL,
  "duration" double precision NOT NULL,
  "student_registration_date" date DEFAULT (now()),
  "status" "project"."status"
);

CREATE TABLE "project"."topic" (
  "id" SERIAL PRIMARY KEY,
  "user_id" int,
  "name" varchar(50) UNIQUE NOT NULL
);

CREATE TABLE "project"."student_grade" (
  "user_id" int,
  "course_id" int,
  "final_mark" double precision NOT NULL DEFAULT 0
);

COMMENT ON COLUMN "project"."courses"."duration" IS 'in months';

ALTER TABLE "project"."courses" ADD FOREIGN KEY ("topic_id") REFERENCES "project"."topic" ("id");

ALTER TABLE "project"."topic" ADD FOREIGN KEY ("user_id") REFERENCES "project"."users" ("id");

ALTER TABLE "project"."student_grade" ADD FOREIGN KEY ("user_id") REFERENCES "project"."users" ("id");

ALTER TABLE "project"."student_grade" ADD FOREIGN KEY ("course_id") REFERENCES "project"."courses" ("id");
