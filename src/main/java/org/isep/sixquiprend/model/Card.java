package org.isep.sixquiprend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Serializable {
    private int number;
    private int bullHeads;
}
