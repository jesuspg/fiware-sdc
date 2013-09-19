package com.telefonica.euro_iaas.sdc.manager;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.impl.ArtifactManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;

/**
 * Unit test suite for ProductManagerChefImpl.
 * 
 * @author Sergio Arroyo
 * 
 */
public class ArtifactManagerChefImplTest extends TestCase {

	private SystemPropertiesProvider propertiesProvider;
	private ProductInstanceDao productInstanceDao;
	private ProductDao productDao;
	private RecipeNamingGenerator recipeNamingGenerator;
	private ChefNodeDao chefNodeDao;
	private ArtifactDao artifactDao;
	private SDCClientUtils sdcClientUtils;
	private ProductInstanceValidator piValidator;
	private Artifact artifact;

	private Product product;
	private ProductInstance expectedProduct;
	private ProductRelease productRelease;
	private OS os;
	private VM host = new VM("fqn", "ip", "hostname", "domain");

	public final static String EXECUTE_COMMAND = "/opt/sdc/scripts/executeRecipes.sh root@hostnamedomain";
	public final static String ASSIGN_UNINSTALL_COMMAND = "/opt/sdc/scripts/assignRecipes.sh hostnamedomain Product::uninstall-server";

	@Before
	public void setUp() throws Exception {
		recipeNamingGenerator = mock(RecipeNamingGenerator.class);
		when(recipeNamingGenerator.getInstallRecipe(any(ProductInstance.class)))
				.thenReturn("Product::server");
		when(
				recipeNamingGenerator
						.getUninstallRecipe(any(ProductInstance.class)))
				.thenReturn("Product::uninstall-server");
		when(
				recipeNamingGenerator
						.getDeployArtifactRecipe(any(ProductInstance.class)))
				.thenReturn("Product::deployac");

		propertiesProvider = mock(SystemPropertiesProvider.class);
		os = new OS("os1", "1", "os1 description", "v1");
		host.setOsType(os.getOsType());

		sdcClientUtils = mock(SDCClientUtils.class);
		sdcClientUtils.execute(host);

		chefNodeDao = mock(ChefNodeDao.class);
		artifactDao = mock(ArtifactDao.class);

		ChefNode cheNode = new ChefNode();
		cheNode.addAttribute("dd", "dd", "dd");
		when(chefNodeDao.loadNode(host.getChefClientName())).thenReturn(cheNode);

		when(chefNodeDao.updateNode((ChefNode) anyObject()))
				.thenReturn(cheNode);

		product = new Product("Product::server", "description");
		productRelease = new ProductRelease("version", "releaseNotes", null,
				product, Arrays.asList(os), null);

		expectedProduct = new ProductInstance(productRelease, Status.INSTALLED,
				host, "vdc");

		productInstanceDao = mock(ProductInstanceDao.class);
		when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(
				expectedProduct);
		when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(
				expectedProduct);
		when(
				productInstanceDao
						.findUniqueByCriteria(any(ProductInstanceSearchCriteria.class)))
				.thenThrow(new NotUniqueResultException());

		productDao = mock(ProductDao.class);
		when(productDao.create(any(Product.class))).thenReturn(product);
		when(productDao.update(any(Product.class))).thenReturn(product);
		when(productDao.load(any(String.class))).thenReturn(product);

		/*
		 * when(productDao.findUniqueByCriteria(
		 * any(ProductSearchCriteria.class))) .thenThrow(new
		 * NotUniqueResultException());
		 */
		piValidator = mock(ProductInstanceValidator.class);

		artifact = new Artifact();
		artifact.setName("artifact");
		Attributes att = new Attributes();
		att.getAttributes().add(new Attribute("key1", "value1"));
		att.getAttributes().add(new Attribute("key2", "value2"));
		artifact.setAttributes(att);
		artifact.setName("artifact");
	}

	@Test
	public void testDeployACWhenEverithingIsOk() throws Exception {
		ArtifactManagerChefImpl manager = new ArtifactManagerChefImpl();
		manager.setProductInstanceDao(productInstanceDao);
		manager.setPropertiesProvider(propertiesProvider);
		manager.setRecipeNamingGenerator(recipeNamingGenerator);
		manager.setChefNodeDao(chefNodeDao);
		manager.setSdcClientUtils(sdcClientUtils);
		manager.setValidator(piValidator);
		manager.setArtifactDao(artifactDao);

		product = new Product("Product::test", "description");
		productRelease = new ProductRelease("version", "releaseNotes", null,
				product, Arrays.asList(os), null);

		expectedProduct = new ProductInstance(productRelease, Status.INSTALLED,
				host, "vdc");

		artifact = new Artifact();
		artifact.setName("artifact2");
		Attributes att = new Attributes();
		att.getAttributes().add(new Attribute("key1", "value1"));
		att.getAttributes().add(new Attribute("key2", "value2"));
		artifact.setAttributes(att);

		manager.deployArtifact(expectedProduct, artifact);

		verify(recipeNamingGenerator, times(1)).getDeployArtifactRecipe(
				any(ProductInstance.class));
		// only one prodcut will be installed, the other one causes error.
		verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
		verify(productInstanceDao, times(1)).update(any(ProductInstance.class));

	//	verify(chefNodeDao, times(1)).loadNode(host.getChefClientName());
	//	verify(chefNodeDao, times(1)).updateNode((ChefNode) anyObject());
		verify(sdcClientUtils, times(2)).execute(host);
		verify(piValidator, times(1)).validateDeployArtifact(expectedProduct);

		assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
		assertEquals("Result", expectedProduct.getArtifacts().size(), 1);

		artifact = new Artifact();
		artifact.setName("artifact3");
		att = new Attributes();
		att.getAttributes().add(new Attribute("key1", "value1"));
		att.getAttributes().add(new Attribute("key2", "value2"));
		artifact.setAttributes(att);

		manager.deployArtifact(expectedProduct, artifact);
		assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
		assertEquals("Result", expectedProduct.getArtifacts().size(), 2);

	}

	@Test
	public void testDeployACNotArtefact() throws Exception {
		ArtifactManagerChefImpl manager = new ArtifactManagerChefImpl();
		manager.setProductInstanceDao(productInstanceDao);
		manager.setPropertiesProvider(propertiesProvider);
		manager.setRecipeNamingGenerator(recipeNamingGenerator);
		manager.setChefNodeDao(chefNodeDao);
		manager.setSdcClientUtils(sdcClientUtils);
		manager.setValidator(piValidator);
		manager.setArtifactDao(artifactDao);

		product = new Product("Product::test", "description");
		productRelease = new ProductRelease("version", "releaseNotes", null,
				product, Arrays.asList(os), null);

		expectedProduct = new ProductInstance(productRelease, Status.INSTALLED,
				host, "vdc");

		artifact = new Artifact();
		artifact.setName("artifact2");

		manager.deployArtifact(expectedProduct, artifact);

		verify(recipeNamingGenerator, times(1)).getDeployArtifactRecipe(
				any(ProductInstance.class));
		verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
		verify(productInstanceDao, times(1)).update(any(ProductInstance.class));
		verify(piValidator, times(1)).validateDeployArtifact(expectedProduct);

		assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
		assertEquals("Result", expectedProduct.getArtifacts().size(), 1);

	}

	@Test
	public void testUndeployAC() throws Exception {
		ArtifactManagerChefImpl manager = new ArtifactManagerChefImpl();
		manager.setProductInstanceDao(productInstanceDao);
		manager.setPropertiesProvider(propertiesProvider);
		manager.setRecipeNamingGenerator(recipeNamingGenerator);
		manager.setChefNodeDao(chefNodeDao);
		manager.setSdcClientUtils(sdcClientUtils);
		manager.setValidator(piValidator);
		manager.setArtifactDao(artifactDao);

		product = new Product("Product::test", "description");
		productRelease = new ProductRelease("version", "releaseNotes", null,
				product, Arrays.asList(os), null);

		expectedProduct = new ProductInstance(productRelease, Status.INSTALLED,
				host, "vdc");

		artifact = new Artifact();
		artifact.setName("artifact2");
		expectedProduct.setArtifact(artifact);

		manager.undeployArtifact(expectedProduct, "artifact2");

		assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
		assertEquals("Result", expectedProduct.getArtifacts().size(), 0);

	}
}
