import os
import re

input_file = r"d:\SocialMediaProject\Instagram-Social-\docs\tasks\phase-2-posts-media.md"
output_dir = r"d:\SocialMediaProject\Instagram-Social-\docs\tasks\phase-2-posts-media"

os.makedirs(output_dir, exist_ok=True)

with open(input_file, 'r', encoding='utf-8') as f:
    content = f.read()

parts = re.split(r'(?m)^### (TASK-2\.\d+.*)$', content)

header = parts[0].strip()
with open(os.path.join(output_dir, "README.md"), "w", encoding="utf-8") as f:
    f.write(header + "\n")

for i in range(1, len(parts), 2):
    title_line = parts[i].strip()
    body = parts[i+1].strip()
    
    task_id_match = re.match(r'(TASK-2\.\d+)', title_line)
    if not task_id_match:
        continue
    task_id = task_id_match.group(1)
    
    slug = title_line.replace(' — ', '-').replace(': ', '-').replace(' ', '-').lower()
    slug = re.sub(r'[^a-z0-9\-]', '', slug)
    
    filename = f"{slug}.md"
    filepath = os.path.join(output_dir, filename)
    
    # Extract file locations
    files = re.findall(r'`([^`]+\.(?:java|ts|tsx))`', body)
    file_locations = ""
    if files:
        file_locations = "## File Location\n\n```\n"
        for file in files:
            if file.endswith('.java'):
                # Guess location based on package names commonly used
                if "domain/model" in file or "domain/exception" in file or "domain/port" in file or "domain/service" in file:
                    path = "backend/src/main/java/com/instagram/" + file.replace("backend/.../", "")
                elif "JpaEntity" in file:
                    path = "backend/src/main/java/com/instagram/infrastructure/persistence/entity/" + file
                elif "JpaRepository" in file:
                    path = "backend/src/main/java/com/instagram/infrastructure/persistence/repository/" + file
                elif "Adapter" in file:
                    path = "backend/src/main/java/com/instagram/infrastructure/persistence/adapter/" + file
                elif "Controller" in file:
                    path = "backend/src/main/java/com/instagram/infrastructure/web/controller/" + file
                elif "Request" in file or "Response" in file:
                    path = "backend/src/main/java/com/instagram/infrastructure/web/dto/" + file
                elif "UseCase" in file:
                    path = "backend/src/main/java/com/instagram/domain/port/in/" + file
                elif "Test" in file or "IT" in file:
                    path = "backend/src/test/java/com/instagram/" + file
                else:
                    path = "backend/src/main/java/com/instagram/" + file.replace("backend/.../", "")
                file_locations += path + "\n"
            else:
                path = file
                if not path.startswith("frontend/"):
                    path = "frontend/src/" + path
                file_locations += path + "\n"
        file_locations += "```\n\n"

    desc = title_line.split('—')[-1].strip()
    
    task_content = f"# {title_line}\n\n"
    task_content += f"## Overview\n\nImplement the {desc} for Phase 2.\n\n"
    task_content += f"## Requirements\n\n- Implement all requirements exactly as specified in the checklist.\n- Follow Hexagonal Architecture principles (for backend).\n- Adhere to the existing design system and patterns.\n\n"
    
    if file_locations:
        task_content += file_locations
        
    task_content += "## Notes\n\n- Ensure correct imports and avoid framework lock-in inside the `domain` module.\n- Use `react-hook-form` and standard Material UI components for the frontend.\n\n"
    
    task_content += "## Checklist\n\n"
    task_content += body + "\n"
    
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(task_content)

print("Done")
