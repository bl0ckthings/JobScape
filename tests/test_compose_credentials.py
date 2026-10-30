import json
import subprocess
import unittest
from pathlib import Path


class ComposeCredentialsTest(unittest.TestCase):
    def test_entity_manager_uses_mysql_root_credentials(self):
        root = Path(__file__).resolve().parents[1]
        config = json.loads(
            subprocess.check_output(
                ["docker", "compose", "--env-file", ".env.example", "config", "--format", "json"],
                cwd=root,
            )
        )
        database = config["services"]["db"]["environment"]
        entity_manager = config["services"]["entity-manager"]["environment"]

        self.assertEqual("root", entity_manager["SPRING_DATASOURCE_USERNAME"])
        self.assertTrue(
            entity_manager["SPRING_DATASOURCE_PASSWORD"]
            == database["MYSQL_ROOT_PASSWORD"],
            "Entity Manager must use the configured MySQL root password",
        )


if __name__ == "__main__":
    unittest.main()
