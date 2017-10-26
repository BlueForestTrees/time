exports.AnagrammeService = class AnagrammeService {

    maxTries;
    intermediateMixCount;
    current = null;
    results = null;

    init = (base, maxTries, intermediateMixCount) => {
        this.current = base.split("");
        this.results = [];
        this.maxTries = maxTries || 10;
        this.intermediateMixCount = intermediateMixCount || 3;
    };

    next = () => {

        for(let i = 0; i < this.intermediateMixCount; i++){
            this.mix();
        }

        for(let i = 0; i < this.maxTries; i++){
            let value = this.mix();
            if(!this.results.indexOf(value)>-1){
                this.results.push(value);
                return value;
            }
        }
    };

    mix = () => {
        const pos1 = this.rand();
        const pos2 = this.rand();
        const temp = this.current[pos1];
        this.current[pos1] = this.current[pos2];
        this.current[pos2] = temp;
        return this.current.join("");
    };

    rand = () => {
        return Math.floor(Math.random() * (current.length + 1));
    };


};