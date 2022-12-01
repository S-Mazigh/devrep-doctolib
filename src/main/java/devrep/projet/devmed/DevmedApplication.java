package devrep.projet.devmed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/*
 * Pour faire en sorte que dans un service qui appelle plusieurs méthode d'un
 * répository soit elles sont toutes executées ou bien aucunes! Par défaut
 * spring active la transaction par méthode de repo
 */
/*
 * Une fois cette option désactivée, cela signifie qu’un appel à une méthode de
 * repository qui effectue une modification sur la base de données devra être
 * appelée dans le cadre d’une transaction ou sinon l’appel échouera.
 */
@EnableJpaRepositories(enableDefaultTransactions = false)
public class DevmedApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevmedApplication.class, args);
	}

}
