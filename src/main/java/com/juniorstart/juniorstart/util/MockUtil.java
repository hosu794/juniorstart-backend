package com.juniorstart.juniorstart.util;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class MockUtil {

    public static PageImpl createMockPage(List<?> list) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        int total = list.size();
        int start = Math.toIntExact(pageRequest.getOffset());
        int end = Math.min(start + pageRequest.getPageSize(), total);

        List<?> output = new ArrayList<>();

        if(start <= end) {
            output = list.subList(start, end);
        }

        return new PageImpl<>(output, pageRequest, total);
    }
}

