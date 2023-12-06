from setuptools import setup, find_packages

# File di setup per costruire il pacchetto dell'applicazione python
setup(
    name="user_MTM",
    version="1.0.3.1",
    description="Modulo di test utente per l'accesso al database mysql",
    author="MTM_group",
    packages=find_packages(),
    install_requires=[
        "mysql-connector-python",
    ],
)
