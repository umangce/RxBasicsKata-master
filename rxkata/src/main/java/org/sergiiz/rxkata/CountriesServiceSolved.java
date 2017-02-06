package org.sergiiz.rxkata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import io.reactivex.Observable;
import io.reactivex.Single;

class CountriesServiceSolved implements CountriesService {

    @Override
    public Single<String> countryNameInCapitals(Country country) {
        return Observable.just(country.name)
                .map(String::toUpperCase)
                .firstOrError();
    }

    public Single<Integer> countCountries(List<Country> countries) {
        return Observable.just(countries.size())
                .firstOrError();
    }

    public Observable<Long> listPopulationOfEachCountry(List<Country> countries) {
        return Observable.fromIterable(countries)
                .map(country -> country.population)
                .flatMap(Observable::just);
    }

    @Override
    public Observable<String> listNameOfEachCountry(List<Country> countries) {
        return Observable.fromIterable(countries)
                .map(country -> country.name)
                .flatMap(Observable::just);
    }

    @Override
    public Observable<Country> listOnly3rdAnd4thCountry(List<Country> countries) {
        return Observable.merge(Observable.just(countries)
                        .map(countryList -> Observable.fromIterable(countryList).elementAt(2).blockingGet()),
                Observable.just(countries)
                        .map(countryList -> Observable.fromIterable(countryList).elementAt(3).blockingGet()));
    }

    @Override
    public Single<Boolean> isAllCountriesPopulationMoreThanOneMillion(List<Country> countries) {
        return Observable.fromIterable(countries)
                .map(country -> country.population > 1000000)
                .reduce((aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .toSingle();
    }


    @Override
    public Observable<Country> listPopulationMoreThanOneMillion(List<Country> countries) {
        return Observable.fromIterable(countries)
                .filter(country -> country.population > 1000000);
    }

    @Override
    public Observable<Country> listPopulationMoreThanOneMillion(FutureTask<List<Country>> countriesFromNetwork) {
        try {
            return Observable.fromIterable(countriesFromNetwork.get())
                    .filter(country -> country.population > 1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Observable<String> getCurrencyUsdIfNotFound(String countryName, List<Country> countries) {
        return Observable.fromIterable(countries)
                .filter(country -> country.name.equals(countryName))
                .map(country -> country.currency)
                .single("USD")
                .toObservable();
    }

    @Override
    public Observable<Long> sumPopulationOfCountries(List<Country> countries) {
        return Observable.fromIterable(countries)
                .map(country -> country.population)
                .reduce((aLong, aLong2) -> aLong + aLong2)
                .toObservable();
    }

    @Override
    public Single<Map<String, Long>> mapCountriesToNamePopulation(List<Country> countries) {
        return Observable.just(countries)
                .map(countryList -> {
                    Map<String, Long> map = new HashMap<>();
                    Observable.fromIterable(countries)
                            .blockingForEach(country -> map.put(country.name, country.population));
                    return map;
                })
                .firstOrError();
    }
}
