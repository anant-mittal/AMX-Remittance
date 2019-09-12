function tabify(response) {
    var table;

    if (response.aggregations) {
        const tree = collectBucket(response.aggregations);
        table = flatten(tree);

    } else if (response.hits) {
        table = response.hits.hits.map((d) => d._source);

    } else if (Array.isArray(response)) {
        table = response;

    } else {
        throw new Error("Tabify() invoked with invalid result set. Result set must have either 'aggregations' or 'hits' defined.");
    }

    return table;
}


function collectBucket(node, stack=[]) {
    if (!node)
        return;
    
    const keys = Object.keys(node);
    
    for(let i = 0; i < keys.length; i++) {
        const key = keys[i];
        const value = node[key];
        if (typeof value === 'object' && value !== null) {
            if ("hits" in value && Array.isArray(value.hits) && value.hits.length === 1) {
                if ("sort" in value.hits[0]) {
                    value.hits[0]._source['sort'] = value.hits[0].sort[0];
                }
                return value.hits[0]._source;
            }

            if (Array.isArray(value)) {
                return extractTree(value, [...stack, key]);
            }

            // Here we are sure to have an object
            if (key === "buckets" && Object.keys(value).length > 1)
            {
                return extractBuckets(value, [...stack, key]);
            }

            return collectBucket(value, [...stack, key]);
        }

        if (key === "value" && typeof value !== "object" && stack.length === 1) {
            let collectedObject = collectBucket({[stack[0]]: value});
            node = collectedObject;
        }
    }

    return node;
}