package com.bonsainet.bonsai.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class GeneralControllerHelper {

  public static Pageable getPageableFromRequest(List<String> sort, List<String> dir, int page, int size,
      String mainClass, List<String> fieldsToExclude,
      Optional<String> childClass, Optional<String> childClassPrefix,
      Optional<String> sortByDefault) {

    // TODO this is ok, but sorting by non-indexed fields could become a problem

    String[] allFieldNames;
    try {
      String[] fieldNames = Stream.of(Class.forName(mainClass).getDeclaredFields())
          .map(Field::getName)
          .filter(fieldName -> !fieldsToExclude.contains(fieldName))
          .toArray(String[]::new);
      String[] childFieldNames = {};
      if (childClass.isPresent() && childClassPrefix.isPresent()) {
        childFieldNames = Stream.of(Class.forName(childClass.get()).getDeclaredFields())
            .map(f -> childClassPrefix.get() + "." + f.getName())
            .toArray(String[]::new);
      }
      allFieldNames = Stream.of(fieldNames, childFieldNames)
          .flatMap(Stream::of)
          .toArray(String[]::new);
    } catch (ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
      return null;
    }

    ArrayList<Order> sortBy = new ArrayList<>();
    if (sort != null) {
      for (int i = 0; i < sort.size(); i++) {
        String sortItem = sort.get(i);
        Sort.Direction sortDir = Sort.Direction.ASC;
        if (dir != null) {
          if (dir.size() > i) {
            if (dir.get(i).equalsIgnoreCase("DESC")) {
              sortDir = Sort.Direction.DESC;
            }
          }
        }
        List<String> f = Arrays.stream(allFieldNames)
            .filter(fieldName -> fieldName.equalsIgnoreCase(sortItem))
            .collect(Collectors.toList());
        if (!f.isEmpty()) {
          sortBy.add(new Sort.Order(sortDir, f.get(0)));
        }
      }
    }
    if (sortByDefault.isPresent()) {
      sortBy.add(new Sort.Order(Sort.Direction.ASC, sortByDefault.get()));
    }

    Sort sortFinal = Sort.by(sortBy);
    if (size < 1) {
      size = 1;
    }
    if (size > 100) {
      size = 100;
    }
    if (page < 0) {
      page = 0;
    }

    return PageRequest.of(page, size, sortFinal);
  }
}
