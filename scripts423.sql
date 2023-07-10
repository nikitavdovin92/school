SELECT s.name, s.age FROM students s LEFT JOIN faculties.f on s.faculty_id = f.id;
SELECT s.name, a.id FROM students s INNER JOIN avatar a on s.id = a.student_id;
