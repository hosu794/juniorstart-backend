package com.juniorstart.juniorstart.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponse<T> {

    private List<T> content;

    private int page;

    private int size;

    private long totalElement;

    private int totalPages;

    private boolean last;

}