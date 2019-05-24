package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.InvoiceMapper;
import cn.neuedu.his.model.Invoice;
import cn.neuedu.his.service.InvoiceService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class InvoiceServiceImpl extends AbstractService<Invoice> implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;

}
