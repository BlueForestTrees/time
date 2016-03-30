use CAM::PDF;
    
    my $pdf = CAM::PDF->new('/home/slimane/time/Histoire/data/sources/novah-harari-sapiens/origin/sapiens.pdf');
   
    my $page = $pdf->getPageContent(10);

    print $page;
