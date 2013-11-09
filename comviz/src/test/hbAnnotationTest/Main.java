package test.hbAnnotationTest;



public class Main {

	public static void main(String[] args) {

		CityDAO cityDAO = new CityDAO();

		long cityId1 = cityDAO.saveCity("New York");
		long cityId2 = cityDAO.saveCity("Rio de Janeiro");
		long cityId3 = cityDAO.saveCity("Tokyo");
		long cityId4 = cityDAO.saveCity("London");

		cityDAO.listCities();

		cityDAO.updateCity(cityId4, "Paris");

		cityDAO.deleteCity(cityId3);

		cityDAO.listCities();
	}
}
