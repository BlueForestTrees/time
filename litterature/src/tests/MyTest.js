import TestCase from 'js-unit/core/TestCase';

export default class MyTest extends TestCase {

    /** @test */
    testService = () => {
        this.assertCount("slimane");
    };

}