class AnagrammeService {

    current: String[];
    results: String[];

    start = (base: String): void => {
        this.current = base.split("");
        this.run();
    };

    run = () => {
        setTimeout(0, () => {
            while (!this.stopRequest) {
                const found: String = this.mix();
                if (!this.results.contains(found)) {
                    this.results.push(found);
                }
            }
            this.stopRequest = false;
        });
    };

    mix = (): String => {
        const pos1: Number = this.rand();
        const pos2: Number = this.rand();
        const temp: String = this.current[pos1];
        this.current[pos1] = this.current[pos2];
        this.current[pos2] = temp;
        return this.current.join();
    };

    rand = () => {
        return Math.floor(Math.random() * (current.length + 1));
    };

}

export default new AnagrammeService();