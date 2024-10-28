import CompanyEduOneImg from '@assets/company-edutech1.jpg';
import CompanyEduTwoImg from '@assets/company-edutech2.jpg';
import CompanyEduThreeImg from '@assets/company-edutech3.jpg';
import CompanyEduFourImg from '@assets/company-edutech4.jpg';
import CompanyHeroImg from '@assets/company-hero.png';
import CompanyProductOneImg from '@assets/company-product1.png';
import CompanyProductTwoImg from '@assets/company-product2.png';
import CompanyServiceImg from '@assets/company-service.png';
import CompanyStatus from '@assets/company-status.png';
import Container from '@components/common/container';

function About() {
  return (
    <div>
      <Container>
      <h1 className="text-4xl font-medium leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight md:ml-40" data-aos="zoom-y-out" >CEO Greetings</h1>
      <div className="flex flex-col md:flex-row w-full mt-4 items-center justify-center text-center md:text-left">
      <div className=" md:w-2/12 p-12">
        <h2 className="text-2xl text-red-400 font-light md:text-3xl mb-4 md:ml-10 md:-rotate-90 md:mb-1/1 whitespace-nowrap">Company Introduction</h2>
      </div>
      <div className=" md:w-5/6 bg-white shadow-lg rounded-lg p-6">
        <p className="text-2xl text-yellow-600 mb-4 md:text-left font-serif font-bold" data-aos="zoom-y-out" data-aos-delay="200">
          We,<br/>Work for you.
          </p>
        <p className="text-xl text-red-700 mb-4 md:text-left font-bold" data-aos="zoom-y-out" data-aos-delay="250">
          Important is Bine each other.
        </p>
        <p className="text-gray-700 mb-4" data-aos="zoom-y-out">
          The GrapeBine (TGB) has been growing with many significant partners,
          such as Universities, international schools, laboratories in many different countries,
          collaborating with Municipal governments in South Korea and abroad.
        </p>
        <p className="text-gray-700 mb-4" data-aos="zoom-y-out">
          Most important value we cherish is &apos;Being Together&apos; to make a difference.
          Impossible is to make things great by oneself, but surely possible with each other.
        </p>
        <p className="text-gray-700 mb-4" data-aos="zoom-y-out">
          The GrapeBine is connected not only with competent partners,
          but sincerely with God who I believe is the only provider of our business.
          No branch can bear fruit by itself; it must remain in the Vine.
        </p>
        <p className="text-gray-700 mb-4" data-aos="zoom-y-out" data-aos-delay="250">
          Join us.
        </p>
        <div data-aos="zoom-y-out" data-aos-delay="250">
        <img src={CompanyHeroImg} alt="Hero" className="flex w-full"/>
        </div>
      </div>
    </div>
    </Container>
    <Container>
      <h1 className="text-4xl font-light leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight md:ml-40 mb-14">B U S I N E S S &nbsp;&nbsp; A R E A</h1>
      <p className="text-gray-700 mb-4 md:ml-40 md:mb-10">
          Most important value we cherish is &apos;Being Together&apos; to make a difference.<br/>
          Impossible is to make things great by oneself, but surely possible with each other.<br/>
          We provide you with&nbsp;all the helps&nbsp;you need&nbsp;for&nbsp;your successful business.
        </p>
    <Container className="flex flex-wrap mb-20 lg:gap-10 lg:flex-nowrap ">
        <div
          className="flex items-center justify-center w-full lg:w-1/2">
          <div>
            <hr className="my-4 border-red-700" style={{ borderWidth: '1.5px' }}/>
            <p className="text-xl text-gray-700 mb-4">
              국제학교 설립 <span className='font-light text-red-600'>K-12 International Schools</span>
            </p>
          <img
              src={CompanyEduOneImg}
              alt="Benefits"
              className="object-cover"
            />
            <p className="text-lg text-gray-700 mt-4">
            • Government Accredited K-12 School
            </p>
            <p className="text-lg text-gray-700 mt-4">
            • SCSA of Western Australia
            </p>
          </div>
        </div>
        <div
          className="flex items-center justify-center w-full lg:w-1/2">
          <div>
            <hr className="my-4 border-red-700" style={{ borderWidth: '1.5px' }}/>
            <p className="text-xl text-gray-700 mb-4">
              대학 부설 연구소 유치 <span className='font-light text-red-600'>Laboratories</span>
            </p>
          <img
              src={CompanyEduTwoImg}
              alt="Benefits"
              className="object-cover"
            />
            <p className="text-lg text-gray-700 mt-2">
            • Vanderbilt University
            </p>
            <p className="text-lg text-gray-700 mt-2">
            • UC Irvine
            </p>
            <p className="text-lg text-gray-700 mt-2">
            • Western Australia University
            </p>
          </div>
        </div>
      </Container>
        <Container className="flex flex-wrap mb-20 lg:gap-10 lg:flex-nowrap -mt-10">
        <div
              className="flex items-center justify-center w-full lg:w-1/2">
              <div>
                <hr className="my-4 border-red-700" style={{ borderWidth: '1.5px' }}/>
                <p className="text-xl text-gray-700 mb-4">
                  대학교 유치 <span className='font-light text-red-600'>Universities</span>
                </p>
              <img
                  src={CompanyEduThreeImg}
                  alt="Benefits"
                  className="object-cover"
                />
                <p className="text-lg text-gray-700 mt-4">
                • Minerva University<br/>• University of Missouri<br/>• University of Virginia<br/>• Vocational College
                </p>
              </div>
            </div>
            <div
              className="flex items-center justify-center w-full lg:w-1/2">
              <div>
                <hr className="my-4 border-red-700" style={{ borderWidth: '1.5px' }}/>
                <p className="text-xl text-gray-700 mb-4">
                  투자 유치 <span className='font-light text-red-600'>Fund Raising</span>
                </p>
              <img
                  src={CompanyEduFourImg}
                  alt="Benefits"
                  className="object-cover"
                />
                <p className="text-lg text-gray-700 mt-4">
                • National Subsidy Program<br/>• Municipal Subsidy Program<br/>• Local University of South Korea<br/>• Corporate Sponsorship
                </p>
              </div>
            </div>
        </Container>
      </Container>
      <Container className="-mt-20">
        <h1 className="text-4xl font-light leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight md:ml-40">E D U T E C H</h1>
        <img src={CompanyProductOneImg} alt="Hero" className="flex w-full -mt-10"/>
        <img src={CompanyServiceImg} alt="Hero" className="flex w-full -mt-10"/>
        <img src={CompanyProductTwoImg} alt="Hero" className="flex w-full -mt-10"/>
      </Container>
      <Container>
        <h1 className="text-4xl font-light leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-4xl xl:leading-tight md:ml-40">O U R <br/>P A R T N E R</h1>
        <img src={CompanyStatus} alt="Hero" className="flex w-full"/>
      </Container>
    </div>
  );
} 

export default About;