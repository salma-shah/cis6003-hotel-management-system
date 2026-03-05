package mapper;

import dto.BillDTO;
import entity.Bill;

public class BillMapper {
    public static BillDTO toBillDTO(Bill bill) {
        if (bill == null) {
            return null;
        }

        return new BillDTO(bill.getId(), bill.getResId(), bill.getGuestId(),bill.getStayCost(),bill.getTax(), bill.getDiscount(),bill.getTotalAmount());
    }

    public static Bill toBill(BillDTO billDTO) {
        if (billDTO == null) {
            return null;
        }

        return new Bill(billDTO.getId(), billDTO.getResId(), billDTO.getGuestId(), billDTO.getStayCost(), billDTO.getTax(), billDTO.getDiscount(), billDTO.getTotalAmount());
    }
}
