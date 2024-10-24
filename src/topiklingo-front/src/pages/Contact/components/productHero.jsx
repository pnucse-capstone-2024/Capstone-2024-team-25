import Container from "@components/common/container";

function ProductHero() {
  return (
    <div className="bg-gray-200 bg-opacity-50" data-aos="zoom-y-out">
        <Container className="flex flex-wrap">
            <div className="flex items-center w-full">
                <div className="w-full">
                <h1 className="text-4xl font-bold leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight" data-aos="zoom-y-out" data-aos-delay="100">
                    Technology and product alliance
                </h1>
                <p className="py-3 text-xl text-gray-500 lg:text-xl xl:text-2xl" data-aos="zoom-y-out" data-aos-delay="150">
                    Topik Mock Service offers a variety of pricing plans to suit your needs. Get started with our free plan or upgrade to a paid plan for additional features.
                </p>
                </div>
            </div>
        </Container>
    </div>
  );
}

export default ProductHero;